(function () {
    "use strict";

    var app = angular.module('App');

    app.component('menu.component', {
        templateUrl: '/app/components/menu/menu.component.html',
        controllerAs: 'ctrl',
        controller: function ($state, STATE) {
            var ctrl = this;
            ctrl.state = 'waiting';
            ctrl.STATE = STATE;
            
            ctrl.$onInit = function () {
                ctrl.state = 'ready';
            };
            
            ctrl.goTo = function(state){
                $state.go(state);    
            };
        }});
})();
