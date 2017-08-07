const initView = Symbol('initView');

class listTicketHallCtrl {

    constructor($scope, $http,testDataService)  {
        this.$scope = $scope;

        this.$http = $http;
        
        this.importdata = testDataService.price_batch_import_data;

        this[initView]();
    };

    [initView]() {
        console.debug('listTicketHallCtrl initView');
        console.debug(`listTicketHallCtrl this.theme = ${this.theme}`);

        this.$scope.message = "TicketHall page."
   };

   onRowChanged(quot){
       console.debug(quot);
       quot.contactDto.name = "qqq";
   }

   onDblClick(quot){
       console.debug(quot);
       quot.contactDto.name = "ddddd";
   }

   onDelItem(id){
       console.debug("del id = " + id);
   }
};

let listTicketHall = () => {
    return {
        template: require('./template/list_ticket_hall.html'),
        bindings: {
            theme: '@'
        },
        controller: ['$scope', '$http','testDataService', listTicketHallCtrl]
    }
};

export default listTicketHall;