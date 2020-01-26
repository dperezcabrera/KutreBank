(function () {
    "use strict";

    var app = angular.module('App');

    app.constant('STATE', {
        HOME: 'home',
        TRANSFER: 'transfer',
        CODE: 'code',
        SIGN_IN: 'signin',
        SIGN_UP: 'signup'
    });

    app.config(function ($stateProvider, $urlRouterProvider, STATE) {
        $urlRouterProvider.otherwise(STATE.HOME);
        $stateProvider
                .state(STATE.HOME, {url: '/home', component: 'home.component', canActivate: 'AuthGuard'})
                .state(STATE.TRANSFER, {url: '/transfer', component: 'transfer.component', canActivate: 'AuthGuard'})
                .state(STATE.CODE, {url: '/code', component: 'code.component', canActivate: 'AuthGuard'})
                .state(STATE.SIGN_UP, {url: '/signup', component: 'signup.component'})
                .state(STATE.SIGN_IN, {url: '/signin', component: 'signin.component'});
    });

    app.run(function ($transitions) {
        $transitions.onStart({}, function (transition) {
            if (transition.to().canActivate) {
                var guard = transition.injector().get(transition.to().canActivate);
                return guard.canActivate(transition.to());
            }
            return true;
        });
    });
})();

