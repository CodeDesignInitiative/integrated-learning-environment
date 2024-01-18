const form = document.getElementById('new-project-form');
const project_name = document.getElementById('project_name');


const create_local_project = (event) => {
    event.preventDefault()

    let projects = JSON.parse(localStorage.getItem("projects"))

    if (projects === null) {
        projects = [{
            name: project_name.value,
            id: crypto.randomUUID()
        }]
    } else {
        projects.push({
            name: project_name.value,
            id: crypto.randomUUID()
        })
    }

    localStorage.setItem("projects", JSON.stringify(projects))

    console.log(JSON.parse(localStorage.getItem("projects")))

    return false;
}

if (form !== null) {
    form.addEventListener("submit", create_local_project);
}


const project_list = document.getElementById("project-list")

const add_project_to_list = (p) =>
    project_list.innerHTML +=
        `<a class="button project" href="projekt/editor/${p.id}"><h3>${p.name}</h3></a>`

if (project_list !== null) {
    let projects = JSON.parse(localStorage.getItem("projects"))
    projects.map(add_project_to_list)
}


const project_save = () => {
    const html = get_html_value();
    const css = (current_language === "css") ? editor.getValue() : css_stored

    let projects = JSON.parse(localStorage.getItem("projects"))

    const project_id = window.location.pathname.split("/").pop()

    projects.map((p) => {
        if (p.id === project_id) {
            p.html = html
            p.css = css
            return p
        } else {
            return p
        }
    })

    localStorage.setItem("projects", JSON.stringify(projects))
}
