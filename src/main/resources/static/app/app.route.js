(function () {
    "use strict";

    var app = angular.module('App');

    app.constant('HOME_STATE', 'home');
    app.constant('LOGIN_STATE', 'login');

    app.config(function ($stateProvider, $urlRouterProvider, HOME_STATE, LOGIN_STATE) {
        $urlRouterProvider.otherwise(HOME_STATE);
        $stateProvider
                .state(HOME_STATE, {url: '/home', component: 'home.component', canActivate: 'AuthGuard'})
                .state(LOGIN_STATE, {url: '/login', component: 'login.component'});
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

