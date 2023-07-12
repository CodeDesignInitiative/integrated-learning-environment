let current_language = "html";

const host = window.location.origin
const mission_id = window.location.pathname.split("/").pop()
const fetch_url = host + "/api/mission/" + mission_id

console.log(fetch_url)

let mission = undefined;

const shuffleArray = (array) => {
    for (let i = array.length - 1; i > 0; i--) {
        const j = Math.floor(Math.random() * (i + 1));
        [array[i], array[j]] = [array[j], array[i]];
    }
    return array;
}



const fill_mission_data = (mission) => {
    const easy_content = mission["mission/content"][0]
    const blocks = easy_content["mission.content/result"]
    const wrong_blocks = easy_content["mission.content/wrong-blocks"]

    const all_blocks = shuffleArray(blocks.concat(wrong_blocks))

    all_blocks.forEach((child) =>
    {
        console.log(child)
        const elem = document.createElement('code')
        elem.classList.add('tile')
        elem.innerText = child
        selection_list.appendChild(elem)
    })


    console.log()
}


const html_editor = document.getElementById("editor")
const html_base = document.getElementById("html-base").innerText
const css_base = document.getElementById("css-base").innerText
const result = document.getElementById("result").childNodes
const wrong = document.getElementById("wrong").innerText

const output = document.getElementById("output")

// import Sortable from 'lib/sortable.min.js';
const target_list = document.getElementById('target');
const selection_list = document.getElementById('selection');

fetch(host + "/api/mission/" + mission_id)
    .then((res) => res.json())
    .then((json) => {
        mission = json;
        console.log(json);
        fill_mission_data(mission);
    })

// const placeholder = html`<!--<code class="placeholder">?</code>-->`
const placeholder = document.createElement("code")
placeholder.append("?")
placeholder.classList.add("placeholder")

new Sortable(target_list, {
    group: 'shared', // set both lists to same group
    filter: '.placeholder',
    animation: 150,
    onSort:  (evt) => {
        if (evt.to.childElementCount < 1) {
            evt.to.appendChild(placeholder)
        }
        if (evt.to.querySelector(".placeholder") != null
            && evt.from.childElementCount > 1 ) {
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
            && evt.from.childElementCount > 1 ) {
            evt.from.childNodes.forEach(child => child.className === "placeholder" ? evt.from.removeChild(child) : null)

        }
    },
    ghostClass: 'ghost',
    dragClass: 'drag',
    chosenClass: 'chosen'
});

const generate_html = () =>
    Array
        .from(target_list.childNodes)
        .reduce((acc, child) => acc + child.innerText, "")

const generate_css = () => css_base


const updateOutput = () =>
    `<!doctype HTML>
     <html lang="de">
        <head>
        <title>ILE Editor Website</title>
        <style>${generate_css()}</style></head>
        <body>${generate_html()}</body>
     </html>`.replace(/#/g, "%23")


const on_input_change = () => {
    output.src = "data:text/html;charset=utf-8," + updateOutput();
}

on_input_change()

const evaluate_code = () => {
    const easy_content = mission["mission/content"][0]
    const correct_result = easy_content["mission.content/result"]
        .reduce((acc, child) => acc + child, "")

    const entered_result =
        Array
            .from(target_list.childNodes)
            .reduce((acc, child) => acc + child.innerText, "")

            console.log(correct_result)
            console.log(entered_result)
            console.log(entered_result === correct_result)

    if (entered_result === correct_result) {
        party_hard()
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
