const init_tabs = () => {
    if (window.location.pathname.startsWith("/admin/story")) {

        let active_tab = "general";

        document.getElementById("general-btn").addEventListener(
            "click",
            function (event) {
                event.preventDefault();
                open_tab('general')
            });
        document.getElementById("story-btn").addEventListener(
            "click",
            function (event) {
                event.preventDefault();
                open_tab('story')
            });
        document.getElementById("content-easy-btn").addEventListener(
            "click", (e) => {
                e.preventDefault();
                open_tab('content-easy')
            });
        document.getElementById("content-medium-btn").addEventListener(
            "click", (e) => {
                e.preventDefault();
                open_tab('content-medium')
            });
        document.getElementById("content-hard-btn").addEventListener(
            "click",
            (e) => {
                e.preventDefault();
                open_tab('content-hard')
            });

        const open_tab = (tab) => {
            const current_tab = document.getElementById(active_tab);
            current_tab.classList.add("hidden");

            const target = document.getElementById(tab);
            target.classList.remove("hidden");

            document.getElementById(active_tab + "-btn").classList.remove("active")
            document.getElementById(tab + "-btn").classList.add("active")

            active_tab = tab;
        }

    }
}

htmx.on("htmx:afterSettle", (_evt) => init_tabs());

init_tabs()