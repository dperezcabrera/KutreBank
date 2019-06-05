(function () {
    "use strict";

    var app = angular.module('App');

    app.component('home.component', {
        templateUrl: '/app/components/home/home.component.html',
        controllerAs: 'ctrl',
        controller: function () {
            var vm = this;
            vm.state = 'waiting';

            vm.$onInit = function () {
                vm.state = 'ready';
            };

        }});
})();