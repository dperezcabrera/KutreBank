(function () {
    "use strict";

    var app = angular.module('App');

    app.component('header.component', {
        templateUrl: '/app/components/header/header.component.html',
        controllerAs: 'ctrl',
        controller: function (AuthService, $state, STATE) {
            var ctrl = this;
            ctrl.user = {};
            
            ctrl.$onInit = function () {
                ctrl.user = AuthService.getUser();
            };
            
            ctrl.logout = function () {
                AuthService.logout();
                $state.go(STATE.SIGN_IN);
            };
        }});
})();
