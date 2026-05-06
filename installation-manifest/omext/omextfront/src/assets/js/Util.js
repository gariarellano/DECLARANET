function customUpperCase(obj) {
    var start = obj.selectionStart;
    var end = obj.selectionEnd;
    obj.value = obj.value.toUpperCase();
    obj.value = obj.value.replace(/  +/g, ' ');
    obj.setSelectionRange(start, end);
};

function customLowerCase(obj) {
    var start = obj.selectionStart;
    var end = obj.selectionEnd;
    obj.value = obj.value.toLowerCase();
    obj.value = obj.value.replace(/  +/g, ' ');
    obj.setSelectionRange(start, end);
};