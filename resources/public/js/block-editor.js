let current_language = "html";

const host = window.location.origin
const mission_id = window.location.pathname.split("/").pop()
const lang = window.location.pathname.split("/")[1]
const fetch_url = host + "/api/mission/" + mission_id

const evaluate_btn = document.getElementById("evalute-btn")


let mission = undefined;
let mode = undefined;
let hidden_css = undefined;
let hidden_html = undefined;
let difficulty = "easy";

const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}


const fill_mission_data = (mission, difficulty = "easy") => {
    console.log(mission);

    let content;
    switch(difficulty) {
        case "easy": content = mission["mission/content"][0]; break;
        case "medium": content = mission["mission/content"][1]; break;
        case "hard": content = mission["mission/content"][2]; break;
        default: content = mission["mission/content"][0];
    }
    hidden_css = content["mission.content/hidden-css"]
    hidden_html = content["mission.content/hidden-html"]
    const blocks = content["mission.content/result"]
    mode = content["mission.content/mode"];
    const wrong_blocks = content["mission.content/wrong-blocks"]

    const all_blocks = shuffleArray(blocks.concat(wrong_blocks))

    selection_list.innerHTML = "";

    all_blocks.forEach((child) => {
        const elem = document.createElement('code')
        elem.classList.add('tile')
        elem.innerText = child
        selection_list.appendChild(elem)
    })
}


const html_editor = document.getElementById("editor")
// const html_base = document.getElementById("html-base").innerText
// const css_base = document.getElementById("css-base").innerText
// const result = document.getElementById("result").childNodes
// const wrong = document.getElementById("wrong").innerText

const output = document.getElementById("output")
const overlay = document.getElementById("overlay")
const chat = document.getElementById("chat")
const chat_message = document.getElementById("chat-message")

// import Sortable from 'lib/sortable.min.js';
const target_list = document.getElementById('target');
const selection_list = document.getElementById('selection');

fetch(host + "/api/mission/" + mission_id)
    .then((res) => res.json())
    .then((json) => {
        mission = json;
        next_message();
        console.log(json);
        fill_mission_data(mission);
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
        if (evt.to.childElementCount < 1) {
            evt.to.appendChild(placeholder)
        }
        if (evt.to.querySelector(".placeholder") != null
            && evt.from.childElementCount > 1) {
            evt.to.childNodes.forEach(child => child.className === "placeholder" ? evt.to.removeChild(child) : null)
        }

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
    onSort: (evt) => {
        if (evt.from.childElementCount < 1) {
            evt.from.appendChild(placeholder)
        }
        if (evt.from.querySelector(".placeholder") != null
            && evt.from.childElementCount > 1) {
            evt.from.childNodes.forEach(child => child.className === "placeholder" ? evt.from.removeChild(child) : null)

        }
    },
    ghostClass: 'ghost',
    dragClass: 'drag',
    chosenClass: 'chosen'
});

const generate_html = () => {
    const user_input = Array
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
    const user_input = Array
        .from(target_list.childNodes)
        .reduce((acc, child) => acc + child.innerText, "")


    if (hidden_css === "") {
        return user_input
    } else {
        return hidden_css
            .replace("$$placeholder$$", user_input)
    }
}


const updateOutput = () =>
    `<!doctype HTML>
     <html lang="de">
        <head>
        <title>ILE Editor Website</title>
        <style>${mode === "css" ? generate_css() : hidden_css}</style></head>
        <body>${mode === "html" ? generate_html() : hidden_html}</body>
     </html>`.replace(/#/g, "%23")


const on_input_change = () => {
    output.src = "data:text/html;charset=utf-8," + updateOutput();
}

on_input_change()

const evaluate_code = () => {
    let content = mission["mission/content"][0]
    if (difficulty === "medium") {
        content = mission["mission/content"][1]
    } else if (difficulty === "hard") {
        content = mission["mission/content"][2]
    }
    const correct_result = content["mission.content/result"]
        .reduce((acc, child) => acc + child, "")

    const entered_result =
        Array
            .from(target_list.childNodes)
            .reduce((acc, child) => acc + child.innerText, "")

    if (entered_result === correct_result) {
        party_hard()
        overlay.classList.toggle("hidden")
    } else {
        alert("Das scheint nicht richtig zu sein. Versuche es noch einmal anders :)")
    }
}

const party_hard = () =>
    tsParticles.load("tsparticles", {
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

const next_message = () => {
    if (!story_after) {
        if (mission["mission/story-before"].length > 0) {
            if (chat_message.innerText === "") {
                chat_message.innerHTML = marked.parse(mission["mission/story-before"][0]);
            } else {
                if (current_message < mission["mission/story-before"].length - 1) {
                    chat_message.innerHTML = marked.parse(mission["mission/story-before"][current_message + 1])
                    current_message = current_message + 1
                } else {
                    chat.classList.add("hidden");
                    story_after = true;
                    current_message = 0;
                }
            }
        } else {
            chat_message.classList.add("hidden");
        }
    } else {
        if (current_message < mission["mission/story-after"].length) {
            chat_message.innerHTML = marked.parse(mission["mission/story-after"][current_message])
            current_message = current_message + 1
        } else {
            next_mission()
        }
    }
}


const progress = () => {
    overlay.classList.add("hidden");
    chat.classList.remove("hidden");


    if (mission["mission/story-after"].length > 0) {
        next_message()
    } else {
        next_mission()
    }
}

const next_mission = () => {
    console.log("next mission")

    const current_step = mission["mission/step"]
    const current_world = mission["mission/world"]

    fetch(host + "/api/next-mission/" + mission_id)
        .then((res) => res.json())
        .then((json) => {
            let url = host + "/" + lang + "/world/mission/" + json["mission-id"]
            console.log(url);
            window.location.href = url;
        })
}

const change_difficulty = (e) => {
    const difficulty_level = e.target.value;
    fill_mission_data(mission, difficulty_level)
}