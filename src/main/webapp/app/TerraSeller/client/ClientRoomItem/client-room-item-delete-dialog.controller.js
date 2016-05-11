(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomItemDeleteController',ClientRoomItemDeleteController);

    ClientRoomItemDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientRoomItem'];

    function ClientRoomItemDeleteController($uibModalInstance, entity, ClientRoomItem) {
        var vm = this;
        vm.clientRoomItem = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ClientRoomItem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
