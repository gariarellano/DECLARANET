export class Utils {

    static CustomUpperCase(obj) {
        const start = obj.selectionStart;
        const end = obj.selectionEnd;
        obj.value = obj.value.toUpperCase();
        obj.setSelectionRange(start, end);
    }
// var start = this.selectionStart; var end = this.selectionEnd;this.value = this.value.toUpperCase(); this.setSelectionRange(start, end);
}
