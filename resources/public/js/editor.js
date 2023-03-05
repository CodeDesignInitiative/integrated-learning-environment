const editor = ace.edit("editor");
editor.setTheme("ace/theme/one_dark");
editor.session.setMode("ace/mode/html");

const html_editor = document.getElementById("editor")
const css_base = document.getElementById("css-base")

const output = document.getElementById("output")


const updateOutput = (code) =>
    `<!DOCTYPE html>
    <html>
        <head><style>${css_base.innerHTML}</style></head>
        <body>${code}</body>
    </html>`.replace(/#/g, "%23");


const on_input_change = () => {
    const code = editor.getValue()
    console.log("change")
    console.log(updateOutput(code))

    output.src = "data:text/html;charset=utf-8," + updateOutput(code);
}

editor.session.on("change", function (d) {
    on_input_change()
})

on_input_change()