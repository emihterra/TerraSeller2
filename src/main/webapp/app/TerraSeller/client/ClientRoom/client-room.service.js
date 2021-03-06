(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('ClientRoom', ClientRoom);

    ClientRoom.$inject = ['$resource'];

    function ClientRoom ($resource) {
        var resourceUrl =  'api/client-rooms/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete': { method:'DELETE' }
        });
    }
})();
