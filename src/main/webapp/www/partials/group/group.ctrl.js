(function () {
    angular.module('app.controllers')
        .controller('Group',
            ['$scope', 'fin', '$stateParams', '$rootScope', '$ionicPopup', 'commons', Group]);
    function Group($scope, fin, $stateParams, $rootScope, $ionicPopup, commons) {
        var groupId = $stateParams.groupId;
        $scope.refresh = refresh;
        $scope.toggleAddMemberForm=toggleAddMemberForm;
        $scope.refresh();

        function toggleAddMemberForm(){
            $scope.showAddMemberForm=!$scope.showAddMemberForm;
        }
        function refresh() {
            fin.getGroup(groupId).then(function (res) {
                $scope.group = res.data;
                $scope.group.isAdmin = ($scope.group.admin.id == $rootScope.user.id);
            }).finally(function () {
                $scope.$broadcast('scroll.refreshComplete');
            });
        }
    }
})()
