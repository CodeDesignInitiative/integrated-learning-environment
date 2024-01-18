const editor = ace.edit("sandbox-editor");
editor.setTheme("ace/theme/one_dark");
editor.session.setMode("ace/mode/html");
const host = window.location.origin

let current_language = "html";

const html_base = document.getElementById("html-base").innerText
const html_snippet = document.getElementById("html-snippet").innerText
const html_answer = document.getElementById("html-answer").innerText
const css_base = document.getElementById("css-base").innerText


const output = document.getElementById("output")

let html_stored = editor.getValue();
let css_stored = "";

const projects = JSON.parse(localStorage.getItem("projects"))
const project_id = window.location.pathname.split("/").pop()
const project = projects.find((p) => (p.id === project_id))

if (project !== null) {
    html_stored = project.html
    editor.setValue(project.html, 1)
    css_stored = project.css
}

const blank_css =
    `body {
    background-color: white;
    font-family: sans-serif;
}`

const beforeUnloadHandler = (event) => {
    // Recommended
    event.preventDefault();

    // Included for legacy support, e.g. Chrome/Edge < 119
    event.returnValue = true;
};
window.addEventListener("beforeunload", beforeUnloadHandler);


const load_css_base_to_stored = () =>
    (css_base !== "") ? css_stored = css_base : {}

window.onload = (event) => {
    load_css_base_to_stored()
}

const change_language = (lang) => {
    if (current_language === "html") {
        html_stored = editor.getValue();
        if (css_stored === "") {
            if (css_base === "") {
                editor.setValue(blank_css, 1)
            } else {
                editor.setValue(css_base, 1)
            }
        } else {
            editor.setValue(css_stored, 1);
        }
        editor.session.setMode("ace/mode/css");
    } else {
        css_stored = editor.getValue();
        editor.setValue(html_stored, 1);
        editor.session.setMode("ace/mode/html");
    }

    current_language = lang;
    on_input_change()
}


const generate_css = () => {
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
    if (current_language === "html") {
        if (html_base === "") {
            return editor.getValue()
        } else {
            return html_base.replace("$$placeholder$$", html_snippet)
                .replace("$placeholder$", editor.getValue())
        }

    } else {
        if (html_base === "") {
            return html_stored
        } else {
            return html_base.replace("$$placeholder$$", html_snippet)
                .replace("$placeholder$", html_stored)
        }
    }
}


const updateOutput = () =>
    `<!doctype HTML>
     <html>
        <head>
            <base href="${host}/img/story/" />
            <style>${generate_css()}</style>
        </head>
        <body>${generate_html()}</body>
     </html>`.replace(/#/g, "%23")


const on_input_change = () => {
    output.src = "data:text/html;charset=utf-8," + updateOutput();
}

editor.session.on("change", function (d) {
    on_input_change()
})

on_input_change()

// validate input

// ;
const validate = (next) => {
    if (html_answer
            .trim()
            .replace(/ +/g, "")
            .replace(/(\r\n|\n|\r)/gm, "") ===
        editor.getValue()
            .trim()
            .replace(/ +/g, "")
            .replace(/(\r\n|\n|\r)/gm, "")) {
        window.location.href = next;
    } else {
        alert("Deine Eingabe ist nicht ganz korrekt. Schau dir nochmal das Beispiel an. Wenn du nicht weiter weißt drücke auf den Hilfe-Knopf mit der Glühbirne.")
    }
}

const show_help = () =>
    alert("Dein Ergebnis sollte so aussehen:\n\n" + html_answer)


// save project

const save_form = document.getElementById("save-form");
const html_input = document.getElementById("html-input");
const css_input = document.getElementById("css-input");
const id_input = document.getElementById("id");

const params = new Proxy(new URLSearchParams(window.location.search), {
    get: (searchParams, prop) => searchParams.get(prop),
});

const get_html_value = () => {
    if (current_language === "html") {
        return editor.getValue()
    } else {
        return html_stored;
    }
}

const save = () => {
    window.removeEventListener("beforeunload", beforeUnloadHandler);

    html_input.value = get_html_value();
    css_input.value = (current_language === "css") ? editor.getValue() : css_stored

    save_form.submit()

    window.addEventListener("beforeunload", beforeUnloadHandler);
}


