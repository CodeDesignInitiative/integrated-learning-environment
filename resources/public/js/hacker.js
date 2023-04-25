const hacked = localStorage.getItem("hacked");

const body =
    document.getElementsByTagName("body")[0]


const check_password = () => {
    const password_input =
        document.getElementById("password")
    if (password_input.value === "30428501") {
        localStorage.setItem("hacked", "false");
        // Simulate an HTTP redirect:
        window.location.replace("/login");
        // body.classList.remove("hacked");
    }
}

if (hacked !== "false") {
    body.innerHTML =
        `<div class="hacked">
            <h1>🔒</h1>
            <label for="password">Passwort</label>
            <input id="password">
            <button onclick="check_password()">Login</button>
        </div>`
    body.classList.add("hacked");
}
