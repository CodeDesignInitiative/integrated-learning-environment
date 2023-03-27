const editor = ace.edit("editor");
editor.setTheme("ace/theme/one_dark");
editor.session.setMode("ace/mode/html");

let current_language = "html";

const html_editor = document.getElementById("editor")
const html_base = document.getElementById("html-base").innerText
const html_snippet = document.getElementById("html-snippet").innerText
const css_base = document.getElementById("css-base").innerText

const output = document.getElementById("output")

let html_stored = editor.getValue();
let css_stored = "";

const blank_css = `body {
                        background-color: white;
                        font-family: sans-serif;
                    }`

const change_language = (lang) => {
    if (current_language !== lang) {
        if (current_language === "html") {
            html_stored = editor.getValue();
            if (css_stored === "") {
                if (css_base === "") {
                    editor.setValue(blank_css)
                } else {
                    editor.setValue(css_base)
                }
            } else {
                editor.setValue(css_stored);
            }
            editor.session.setMode("ace/mode/css");
        } else {
            css_stored = editor.getValue();
            editor.setValue(html_stored);
            editor.session.setMode("ace/mode/html");
        }

        current_language = lang;
        on_input_change()
    }
}


const generate_css = () => {
    // console.log("Css stored: ", (css_stored === ""))
    // console.log("Css base: ", (css_base === ""))
    // console.log("Css current lang: ", (current_language === "css"))
    if (current_language === "css") {
        return editor.getValue()
    } else {
        if (css_stored === "") {
            if (css_base === "") {
                return blank_css
            } else {
                return css_base
            }
        } else {
            return css_stored
        }

    }
}

const generate_html = () => {
    console.log("Html base: ", (html_base === ""))
    if (current_language === "html") {
        if (html_base === "") {
            return editor.getValue()
        } else {
            return html_base.replace("$$placeholder$$", html_snippet)
            .replace("$placeholder$", editor.getValue())
        }

    } else {
        if (html_base === "") {
            return ""
        } else {
            return html_base.replace("$$placeholder$$", html_snippet)
                .replace("$placeholder$", html_stored)
        }
    }
}


const updateOutput = () =>
    `<!doctype HTML>
     <html>
        <head><style>${generate_css()}</style></head>
        <body>${generate_html()}</body>
     </html>`.replace(/#/g, "%23")


const on_input_change = () => {
    output.src = "data:text/html;charset=utf-8," + updateOutput();
}

editor.session.on("change", function (d) {
    on_input_change()
})

on_input_change()