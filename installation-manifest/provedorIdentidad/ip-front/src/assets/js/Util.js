function CustomUpperCase(obj) {
    const start = obj.selectionStart;
    const end = obj.selectionEnd;
    obj.value = obj.value.toUpperCase();
    obj.value = obj.value.replace(/  +/g, ' ');
    obj.setSelectionRange(start, end);
}

function CustomLowerCase(obj) {
    const start = obj.selectionStart;
    const end = obj.selectionEnd;
    obj.value = obj.value.toLowerCase();
    obj.value = obj.value.replace(/  +/g, ' ');
    obj.setSelectionRange(start, end);
}