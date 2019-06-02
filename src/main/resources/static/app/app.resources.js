(function () {
    "use strict";

    var app = angular.module('App');
    var actions = {update: {method: 'PUT'}};

    app.factory('Users', function ($resource) {
        return $resource('/users/:id', {id: '@id'}, actions);
    }).factory('Login', function ($resource) {
        return $resource('/auth/login', {}, actions);
    }).factory('SignUp', function ($resource) {
        return $resource('/auth/signup', {}, actions);
    });
})();

