(function() {
    'use strict';

    angular
        .module('app.terraSeller')
        .controller('ClientRoomDeleteController',ClientRoomDeleteController);

    ClientRoomDeleteController.$inject = ['$uibModalInstance', 'entity', 'ClientRoom'];

    function ClientRoomDeleteController($uibModalInstance, entity, ClientRoom) {
        var vm = this;
        vm.clientRoom = entity;
        vm.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        vm.confirmDelete = function (id) {
            ClientRoom.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };
    }
})();
