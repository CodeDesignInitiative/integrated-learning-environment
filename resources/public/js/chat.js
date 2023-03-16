const scrollable = document.getElementById('chat');

const messagePushCallback = () =>
    scrollable.scrollTo(0, scrollable.scrollHeight-scrollable.clientHeight)

// for an example
// chat.onMessagePush(messagePushCallback);

window.addEventListener('load', messagePushCallback);