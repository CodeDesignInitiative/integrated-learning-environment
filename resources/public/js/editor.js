const editor = ace.edit("editor");
editor.setTheme("ace/theme/one_dark");
editor.session.setMode("ace/mode/html");

const html_editor = document.getElementById("editor")
const html_base = document.getElementById("html-base").innerText
const html_snippet = document.getElementById("html-snippet").innerText
const css_base = document.getElementById("css-base").innerText

const output = document.getElementById("output")


const updateOutput = (code) => {
    console.log(html_base)
    console.log(html_base.replace("$$placeholder$$", html_snippet))
    return `<!doctype HTML>
     <html>
        <head><style>${css_base}</style></head>
        <body>${html_base.replace("$$placeholder$$", html_snippet)
        .replace("$placeholder$", code)}</body>
     </html>`.replace(/#/g, "%23")
}



const on_input_change = () => {
    const code = editor.getValue()

    output.src = "data:text/html;charset=utf-8," + updateOutput(code);
}

editor.session.on("change", function (d) {
    on_input_change()
})

on_input_change()