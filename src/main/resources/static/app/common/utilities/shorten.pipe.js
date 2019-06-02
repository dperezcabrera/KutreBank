(function () {
    "use strict";

    var app = angular.module('App');
    
    app.filter('shorten', function () {
        return function (text, length) {
            var result = text;
            if (text && text.length > length) {
                result = text.substring(0, length - 3) + "...";
            }
            return result;
        };
    });
})();

