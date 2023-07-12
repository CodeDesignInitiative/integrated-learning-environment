let current_language = "html";

const html_editor = document.getElementById("editor")
const html_base = document.getElementById("html-base").innerText
const css_base = document.getElementById("css-base").innerText
const result = document.getElementById("result").innerText
const wrong = document.getElementById("wrong").innerText

const output = document.getElementById("output")


// import Sortable from 'lib/sortable.min.js';
const target_list = document.getElementById('target');
const selection_list = document.getElementById('selection');

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

const blank_css =
    `body {
    background-color: white;
    font-family: sans-serif;
}`

//
//
// const generate_css = () => {
//     if (current_language === "css") {
//         return editor.getValue()
//     } else {
//         if (css_stored === "") {
//             if (css_base === "") {
//                 return blank_css
//             } else {
//                 return css_base
//             }
//         } else {
//             return css_stored
//         }
//
//     }
// }
//
// const generate_html = () => {
//     if (current_language === "html") {
//         if (html_base === "") {
//             return editor.getValue()
//         } else {
//             return html_base.replace("$$placeholder$$", html_snippet)
//                 .replace("$placeholder$", editor.getValue())
//         }
//
//     } else {
//         if (html_base === "") {
//             return html_stored
//         } else {
//             return html_base.replace("$$placeholder$$", html_snippet)
//                 .replace("$placeholder$", html_stored)
//         }
//     }
// }
//

const generate_html = () => {
    let code = Array.from(target_list.childNodes).reduce(
        (acc, child) => acc + child.innerText,
        ""
    )
    console.log(code)
    return code;
}

const updateOutput = () =>
    `<!doctype HTML>
     <html>
        <body>${generate_html()}</body>
     </html>`.replace(/#/g, "%23")


const on_input_change = () => {
    output.src = "data:text/html;charset=utf-8," + updateOutput();
}
