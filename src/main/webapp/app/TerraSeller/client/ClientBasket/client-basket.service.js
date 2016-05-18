(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('ClientBasket', ClientBasket);

    ClientBasket.$inject = ['$resource'];

    function ClientBasket ($resource) {
        var resourceUrl = 'api/client-baskets/:id';

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
            'update': { method:'PUT' }
        });
    }
})();
