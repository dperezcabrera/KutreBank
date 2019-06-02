(function () {
    "use strict";

    var app = angular.module('App');

    app.service('join', function () {

        function check(initialization) {
            var result = true;
            var prop = Object.keys(initialization);
            for (var i in prop) {
                if (!initialization[prop[i]].ended) {
                    result = false;
                    break;
                }
            }
            return result;
        }

        this.init = function (elements, callback) {
            var initialization = {};
            elements.forEach(function (element) {
                initialization[element] = {
                    ended: false,
                    completed: function () {
                        initialization[element].ended = true;
                        if (check(initialization)) {
                            callback();
                        }
                    }
                };
            });
            return initialization;
        };
    });
})();
