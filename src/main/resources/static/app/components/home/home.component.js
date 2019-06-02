(function () {
    "use strict";

    var app = angular.module('App');

    app.controller('HomeCtrl', function (credentials, $routeParams, $location) {
        var vm = this;
        vm.courses = [];
        vm.state = 'waiting';
        
        vm.init = function() {
            if (credentials.authenticatedUser()) {
                vm.state = 'ready';
            }
        };
        
        vm.init();
    });
})();