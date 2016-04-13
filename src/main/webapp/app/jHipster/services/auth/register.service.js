(function () {
    'use strict';

    angular
        .module('app.jHipster')
        .factory('Register', Register);

    Register.$inject = ['$resource'];

    function Register ($resource) {
        return $resource('api/register', {}, {});
    }
})();
