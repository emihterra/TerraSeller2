(function() {
    'use strict';
    angular
        .module('app.terraSeller')
        .factory('ItemTypeService', ItemTypeService);

    ItemTypeService.$inject = ['$resource'];

    function ItemTypeService ($resource) {
        var resourceUrl =  'api/item-types/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                ignoreErrors: true,
                transformResponse: function (data) {
                    if (data) {
                      data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' }
        });
    }
})();
