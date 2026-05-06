/* ===========================================================
 * jquery-onepage-scroll.js v1.3.1
 * ===========================================================
 * Copyright 2013 Pete Rojwongsuriya.
 * http://www.thepetedesign.com
 *
 * Create an Apple-like website that let user scroll
 * one page at a time
 *
 * Credit: Eike Send for the awesome swipe event
 * https://github.com/peachananr/onepage-scroll
 *
 * License: GPL v3
 *
 * ========================================================== */
// $(document).ready(function() {

function prepareOneScroll() {
    var menu = false;
    var sec = [false, false];
    var cuenta = false;
    sessionStorage.setItem('secctions', total);
    //nextSection
    $("#nextSection").click(() => {
        $(".main").moveDown();
    });



    prepareMenuCuenta();

    // Content Cargando
    // $("#guardaDatos").click(() => {
    //     setTimeout(() => {
    //         setTimeout(function() {
    //             $("#content-cargando").css("display", "flex");
    //             setTimeout(function() {
    //                 // $("#content-cargando").css("display", "none");
    //                 $("#content-cargando").fadeOut("slow", () => {
    //                     $(this).css("display", "none");
    //                 });
    //             }, 3000);
    //         }, 100);
    //     }, 1000);
    // });


    // Abril/Cerrar Menú
    $(".abrirMenu").click(() => {
        menu = !menu;
        if (menu) {
            $(".menu").css("width", "250px");
            $(".menu-nav").css("width", "0px");
            // $(".cuenta-content").css("padding-right","3rem");
            //TODO
            $("#left > span").text("Cerrar");
            $("#left > i").removeClass("fas fa-bars").addClass("fas fa-times");

        } else {
            $(".menu").css("width", "0px");
            $(".menu-nav").css("width", "50px");
            // $(".cuenta-content").css("padding-right","9rem");
            //TODO
            $("#left > span").text("Abrir");
            $("#left > i").removeClass("fas fa-times").addClass("fas fa-bars");
        }
    });


    // Abrir/Cerrar Secciones
    $("#section1").click(() => {

        sec[0] = !sec[0];

        if (sec[0]) {

            $("#section1").next("ul").css({ "height": "700px", "background": "rgb(218, 220, 224)", "overflow": "hidden" });
            $("#section1").addClass("section-active");
            for (var i = 1; i <= sec.length; i++) {
                if (i != 1) {
                    $(`#section${i}`).next("ul").css({ "height": "0px", "background": "transparent" });
                    $(`#section${i}`).removeClass("section-active");

                    sec[i - 1] = false;
                }
            }
        } else {
            $("#section1").next("ul").css({ "background": "transparent", "height": "0px", "overflow": "hidden" });
            $(`#section1`).removeClass("section-active");
        }
    })


    $("#section2").click(() => {
        sec[1] = !sec[1];

        if (sec[1]) {
            $("#section2").next("ul").css({ "height": "350px", "background": "rgb(218, 220, 224)", "overflow": "hidden" });
            $("#section2").addClass("section-active");
            for (var i = 1; i <= sec.length; i++) {
                if (i != 2) {
                    $(`#section${i}`).next("ul").css({ "height": "0px", "background": "transparent" });
                    $(`#section${i}`).removeClass("section-active");

                    sec[i - 1] = false;
                }
            }

        } else {
            $("#section2").next("ul").css({ "background": "transparent", "height": "0px", "overflow": "hidden" });
            $(`#section2`).removeClass("section-active");
        }
    })



    // navegación submenu
    $(".itemnav").click(() => {
        var index = event.target.className.split(' ')[1].replace('item', '').replace('icon', '');
        $(".main").moveTo(index);
        $(`.item${index}`).addClass("menu-nav-active");
        // $(`.i${index}`).addClass("menu-nav-active-2");
        for (var i = 1; i <= total; i++) {
            if (i != index) {
                $(`.item${i}`).removeClass("menu-nav-active");
                // $(`.item${i}`).removeClass("menu-nav-active-2");
            }
        }
    });



    $(".main").onepage_scroll({
        sectionContainer: "section",
        easing: "ease",
        animationTime: 1000,
        pagination: true,
        updateURL: false
    });

}
// });