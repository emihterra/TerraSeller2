(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomDetailController', ClientRoomDetailController);

    ClientRoomDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'entity', 'ClientRoom'];

    function ClientRoomDetailController($scope, $rootScope, $stateParams, entity, ClientRoom) {
        var vm = this;
        vm.clientRoom = entity;
        vm.load = function (id) {
            ClientRoom.get({id: id}, function(result) {
                vm.clientRoom = result;
            });
        };
        var unsubscribe = $rootScope.$on('app.terraSeller:clientRoomUpdate', function(event, result) {
            vm.clientRoom = result;
        });
        $scope.$on('$destroy', unsubscribe);

    }
})();
