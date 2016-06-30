(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('History', History);

    History.$inject = ['$resource'];

    function History ($resource) {
        var resourceUrl =  'api/histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
