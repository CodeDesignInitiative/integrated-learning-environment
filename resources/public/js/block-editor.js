// path data
const host = window.location.origin
const mission_id = window.location.pathname.split("/").pop()
const lang = window.location.pathname.split("/")[1]

// elements
const mission_editor = document.getElementById("mission-editor")
const evaluate_btn = document.getElementById("evalute-btn")
const chat_next_btn = document.getElementById("chat-next-btn")
const phone = document.getElementById("phone")
const explanation_node = document.getElementById("explanation")
const block_editor_target = document.getElementById("block-editor-target")
const block_editor_selection = document.getElementById("block-editor-selection")
const text_editor = document.getElementById("text-editor")

// state
let phone_hidden = false;

const target_list = document.getElementById('target');
const selection_list = document.getElementById('selection');

let mission = undefined;
let is_block_mode = undefined;
let input_type = "html";
let hidden_css = "";
let hidden_html = "";
let difficulty = "easy";
let is_story_mode = undefined;

const is_text_mode = () => !is_block_mode

const editor = ace.edit("text-editor");
editor.setTheme("ace/theme/one_dark");
editor.session.setMode("ace/mode/html");
editor.session.on("change", function (_d) {
    on_input_change()
})

// functions
const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}


const fill_mission_data = (mission, difficulty = "easy") => {
    // switch (difficulty) {
    //     case "easy":
    //         content = mission["mission/content"][lang][0];
    //         break;
    //     case "medium":
    //         content = mission["mission/content"][lang][1];
    //         break;
    //     case "hard":
    //         content = mission["mission/content"][lang][2];
    //         break;
    //     default:
    //         content = mission["mission/content"][lang][0];
    // }
    hidden_css = mission["learning-track-task/hidden-css"]
    hidden_html = mission["learning-track-task/hidden-html"]
    explanation_node.innerHTML = hint_message_to_html(mission["learning-track-task/explanation"])
    const solution = mission["learning-track-task/solution"]
    is_block_mode = mission["learning-track-task/block-mode?"];
    is_story_mode = mission["learning-track/story-mode?"];
    // input_type = mission["mission.content/input-type"];
    if (!is_block_mode) {
        text_editor.classList.remove("hidden")
        block_editor_target.classList.add("hidden")
        block_editor_selection.classList.add("hidden")
        text_editor.classList.remove("hidden")
        editor.setValue("")
    } else {
        editor.setValue("")
        text_editor.classList.add("hidden")
        block_editor_target.classList.remove("hidden")
        block_editor_selection.classList.remove("hidden")

        const wrong_blocks = (mission["learning-track-task/wrong-blocks"] !== "") ? mission["learning-track-task/wrong-blocks"].split('\n') : []

        const all_blocks = shuffleArray(solution.split('\n').concat(wrong_blocks))

        console.log(solution.split('\n'))
        console.log(wrong_blocks)
        console.log(all_blocks)

        selection_list.innerHTML = "";

        all_blocks.forEach((child) => {
            const elem = document.createElement('code')

            elem.classList.add('tile')
            elem.onclick = (e) => swap_block(e, elem)
            elem.innerText = child
            selection_list.appendChild(elem)
        })
    }
    on_input_change()
}

const swap_block = (event, element) => {
    if (element.parentElement.id === "selection") {
        target_list.appendChild(element)
    } else {
        selection_list.appendChild(element)
    }
    on_input_change()
    // element.remove()
}

const output = document.getElementById("output")
const overlay = document.getElementById("overlay")
const chat = document.getElementById("chat")
const chat_messages = document.getElementById("chat-messages")

fetch(host + "/api/learning-track-task/" + mission_id)
    .then((res) => res.json())
    .then((json) => {
        mission = json;
        next_message();
        fill_mission_data(mission);
        // fill_mission_data(mission, document.getElementById("difficulty").value);
        on_input_change();
    })

// const placeholder = html`<!--<code class="placeholder">?</code>-->`
const placeholder = document.createElement("code")
placeholder.append("?")
placeholder.classList.add("placeholder")

new Sortable(target_list, {
    group: 'shared', // set both lists to same group
    filter: '.placeholder',
    animation: 150,
    onSort: (evt) => {
        on_input_change()
    },
    ghostClass: 'ghost',
    dragClass: 'drag',
    chosenClass: 'chosen'

});

new Sortable(selection_list, {
    group: 'shared',
    filter: '.placeholder',
    animation: 150,
    ghostClass: 'ghost',
    dragClass: 'drag',
    chosenClass: 'chosen',
});

const generate_html = () => {
    const user_input = is_text_mode() ?
        editor.getValue() :
        Array
            .from(target_list.childNodes)
            .reduce((acc, child) => acc + child.innerText, "")

    if (hidden_html === "") {
        return user_input
    } else {
        return hidden_html
            .replace("$$placeholder$$", user_input)
    }
}

const generate_css = () => {
    const user_input = is_text_mode() ?
        editor.getValue() :
        Array
            .from(target_list.childNodes)
            .reduce((acc, child) => acc + child.innerText, "")


    if (hidden_css === "") {
        return user_input
    } else {
        return hidden_css
            .replace("$$placeholder$$", user_input)
    }
}


const update_output = () =>
    `<!doctype HTML>
     <html lang="de">
        <head>
        <base href="${host}/img/story/" />
        <title>ILE Editor Website</title>
        <style>${input_type === "css" ? generate_css() : hidden_css}</style></head>
        <body>${input_type === "html" ? generate_html() : hidden_html}</body>
     </html>`.replace(/#/g, "%23")


const on_input_change = () => {
    output.src = "data:text/html;charset=utf-8," + update_output();
}

on_input_change()

const evaluate_code = () => {
    // let content = mission["mission/content"][lang][0]
    // if (difficulty === "medium") {
    //     content = mission["mission/content"][lang][1]
    // } else if (difficulty === "hard") {
    //     content = mission["mission/content"][lang][2]
    // }

    const correct_result =
        !is_block_mode ? mission["learning-track-task/solution"]
            : mission["learning-track-task/solution"].replace(/\r/g, '').replace(/\n/g, '')

    const entered_result =
        is_text_mode() ?
            editor.getValue() :
            Array
                .from(target_list.childNodes)
                .reduce((acc, child) => acc + child.innerText, "")
                .replace(/\r/g, '')
                .replace(/\n/g, '')

    console.log(correct_result)
    console.log(entered_result)

    if (entered_result === correct_result) {
        party_hard();
        // chat_next_btn.classList.remove("hidden")
        story_after = true;
        overlay.classList.toggle("hidden")
    } else {
        alert("Das scheint nicht richtig zu sein. " +
            "Versuche es noch einmal anders :) \n \n" +
            "So sollte es aussehen: \n" +
            correct_result + "\n" +
            mission["learning-track-task/hint"]
        )
    }
}

const party_hard = () =>
    tsParticles.load("tsparticles", {
        "duration": 5,
        "fullScreen": {
            "zIndex": 1
        },
        "particles": {
            "number": {
                "value": 0
            },
            "color": {
                "value": [
                    "#00FFFC",
                    "#FC00FF",
                    "#fffc00"
                ]
            },
            "shape": {
                "type": [
                    "circle",
                    "square"
                ],
                "options": {}
            },
            "opacity": {
                "value": 1,
                "animation": {
                    "enable": true,
                    "minimumValue": 0,
                    "speed": 2,
                    "startValue": "max",
                    "destroy": "min"
                }
            },
            "size": {
                "value": 4,
                "random": {
                    "enable": true,
                    "minimumValue": 2
                }
            },
            "links": {
                "enable": false
            },
            "life": {
                "duration": {
                    "sync": true,
                    "value": 5
                },
                "count": 1
            },
            "move": {
                "enable": true,
                "gravity": {
                    "enable": true,
                    "acceleration": 10
                },
                "speed": {
                    "min": 10,
                    "max": 20
                },
                "decay": 0.1,
                "direction": "none",
                "straight": false,
                "outModes": {
                    "default": "destroy",
                    "top": "none"
                }
            },
            "rotate": {
                "value": {
                    "min": 0,
                    "max": 360
                },
                "direction": "random",
                "move": true,
                "animation": {
                    "enable": true,
                    "speed": 60
                }
            },
            "tilt": {
                "direction": "random",
                "enable": true,
                "move": true,
                "value": {
                    "min": 0,
                    "max": 360
                },
                "animation": {
                    "enable": true,
                    "speed": 60
                }
            },
            "roll": {
                "darken": {
                    "enable": true,
                    "value": 25
                },
                "enable": true,
                "speed": {
                    "min": 15,
                    "max": 25
                }
            },
            "wobble": {
                "distance": 30,
                "enable": true,
                "move": true,
                "speed": {
                    "min": -15,
                    "max": 15
                }
            }
        },
        "emitters": {
            "life": {
                "count": 0,
                "duration": 0.1,
                "delay": 0.4
            },
            "rate": {
                "delay": 0.1,
                "quantity": 150
            },
            "size": {
                "width": 0,
                "height": 0
            }
        }
    });

let current_message = 0
let story_after = false
let pre_mission_chat_done = false

const chat_message_to_html = (msg) =>
    `<div class="chat-message">${marked.parse(msg)} </div>`

const hint_message_to_html = (msg) =>
    `${marked.parse(msg)}`


const enable_show_phone = () => {
    chat.onclick = () => show_phone()
    console.log("Show phone!")
}

const disable_show_phone = () =>
    chat.onclick = {}

const to_image_or_text = (msg) => {
    if (msg.startsWith("[img]")) {
        return `<img src="/img/story/${msg.slice(5)}" alt="${msg}">`
    } else {
        return chat_message_to_html(msg)
    }
}

const next_message = () => {
    if (is_story_mode) {
        if (!story_after) {
            if (!pre_mission_chat_done) {
                if (mission["learning-track-task/messages-before"].length > 0) {
                    if (current_message < mission["learning-track-task/messages-before"].length) {
                        chat_messages.innerHTML += to_image_or_text(mission["learning-track-task/messages-before"][current_message])
                        current_message = current_message + 1
                    } else {
                        chat.classList.add("chat-hidden");
                        pre_mission_chat_done = true;
                        current_message = 0;
                        setTimeout(() => enable_show_phone(), 200)
                    }
                    setTimeout(() => {
                        chat_messages.scrollTop = chat_messages.scrollHeight;
                    }, 200)

                } else {
                    chat_messages.classList.add("chat-hidden");
                }
            } else {
                chat.classList.add("chat-hidden");
                setTimeout(() => enable_show_phone(), 200)
            }
        } else {
            if (current_message < mission["learning-track-task/messages-after"].length) {
                chat_messages.innerHTML += to_image_or_text(mission["learning-track-task/messages-after"][current_message])
                current_message = current_message + 1;
                chat_messages.scrollTop = chat_messages.scrollHeight;
            } else {
                next_mission()
            }
        }
    } else {
        chat.remove()
    }
}


const progress = () => {
    overlay.classList.add("hidden");
    chat.classList.remove("chat-hidden");

    next_mission()
}

const next_mission = () => {
    const current_step = mission["learning-track-task/step"]
    const learning_track = mission["learning-track-task/learning-track"]

    fetch(host + "/api/next-learning-track-task/" + mission_id)
        .then((res) => res.json())
        .then((json) => {
            if (json["status"] === "last_mission") {
                window.location.href = host + "/" + lang + "/world/" + learning_track + "/finished";

            } else {
                window.location.href =
                    host
                    + "/" + lang
                    + "/world/" + learning_track
                    + "/mission/" + json["next-learning-track-task-id"];
            }
        })
}

const change_difficulty = (e) => {
    const difficulty_level = e.target.value;
    fill_mission_data(mission, difficulty_level)
}

const show_phone = () => {
    chat.classList.remove("chat-hidden")
    disable_show_phone()
}

// define a handler
function hide_phone_shortcut(event) {
    if (!phone_hidden && (event.code === 'Escape')) {
        chat.classList.add("chat-hidden");
        setTimeout(() => enable_show_phone(), 200)
    }
}

// register the handler
document.addEventListener('keyup', hide_phone_shortcut, false);