(function () {
    angular.module('app.controllers')
        .controller('Group',
            ['$scope', 'fin', '$stateParams', '$rootScope', '$ionicFilterBar', 'commons', Group]);
    function Group($scope, fin, $stateParams, $rootScope, $ionicFilterBar, commons) {
        var groupId = $stateParams.groupId;
        $scope.refresh = refresh;
        $scope.toggleAddMemberForm = toggleAddMemberForm;
        $scope.refresh();

        fin.findUsers("").then(function(res){
            $scope.users=res.data;
        })

        function toggleAddMemberForm() {
            var filterBarInstance = $ionicFilterBar.show({
                items: $scope.users,
                update: function (filteredItems, filterText) {
                    $scope.users = filteredItems;
                    if (filterText) {
                        console.log(filterText);
                    }
                }
            });
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
