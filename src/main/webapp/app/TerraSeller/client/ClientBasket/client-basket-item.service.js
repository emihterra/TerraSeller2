(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('ClientBasketItem', ClientBasketItem);

    ClientBasketItem.$inject = ['$resource'];

    function ClientBasketItem ($resource) {
        var resourceUrl =  'api/client-basket-items/:id';

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
