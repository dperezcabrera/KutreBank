(function () {
    "use strict";

    var app = angular.module('App');

    app.component('waiting.component', {
        template: '<div class="waiting-box"><img src="images/loading.gif" alt="waiting..."/></div>',
        controllerAs: 'ctrl',
        controller: function () {}});
})();
