const initView = Symbol('initView');

const THEME_NAME = "ssAvalonUi";

const VIRTUAL_DAY = {
    date: '',
    dayOfWeek: '',
    holdingPeriod: '',
    holidayReason: ''
};

class babCalendarCtrl {

    constructor ($scope, configConsts, httpService) {
        this.$scope = $scope;
        this.httpService = httpService;
        this.configConsts = configConsts;
        this[initView]();
    };

    [initView] () {
        console.debug('babCalendarCtrl initView');
        this.theme = THEME_NAME;
        this.isBusy = true;

        let now = new Date();
        this.date = now.format('M月d日');
        this.year = now.format('yyyy年');
        this.week = now.format('EEE');

        this.years = [+now.format('yyyy'), +now.format('yyyy') + 1];
        this.selectYear = this.years[0];

        let months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        this.selectMonth = +now.format('M');
        this.months = months.findWhere(e => e >= this.selectMonth);


        this.itemSource = [
            {$id: 'HALF_YEAR', displayName: '半年', code: 'HALF_YEAR', isDefault: true},
            {$id: 'YEAR', displayName: '一年', code: 'YEAR'}
        ];

        this.$scope.$on('clickDate', (event, item) => {
            let date = item.date;
            this.clickDate = date;

            let now = new Date(date);
            this.date = now.format('M月d日');
            this.year = now.format('yyyy年');
            this.week = now.format('EEE');
            this.result = item.holdingPeriod;
            this.onVmChanged(true);
        });
    };

    onVmChanged (a) {
        if (!this.period || !this.period.code) return;
        if (!this.selectMonth || !this.selectYear) return;

        let period = this.period.code || 'HALF_YEAR';
        let now;
        if (a) {
            now = this.clickDate;
        } else {
            let time = new Date();
            time.setFullYear(+this.selectYear);
            time.setMonth(+this.selectMonth - 1, 1);
            now = time.getTime();
        }

        let param = {"date": now, "period": period};
        this.httpService.postService(this.configConsts.bill_calendar_for_month, param).then(res => {
            if (res.return_code === 0 && res.return_message === 'Success' && res.result_count !== 0) {
                if (res.result && angular.isArray(res.result) && res.result.length === 1) {
                    let data = res.result[0];

                    this.endTime = new Date(data.maturityDate).format('yyyy-MM-dd');
                    let startDate = angular.copy(data.invoiceMonthDays);
                    let endDate = angular.copy(data.maturityMonthDays);

                    if (!this.clickDate) {
                        this.startTime = new Date().format('yyyy-MM-dd');
                        let startItem = startDate.findItem(e => new Date(e.date).format('yyyy-MM-dd') === new Date().format('yyyy-MM-dd'));
                        if (startItem) {
                            if (!startItem.holidayReason) {
                                startItem.selected = true;
                            }
                        }
                    } else {
                        this.startTime = new Date(this.clickDate).format('yyyy-MM-dd');
                        let startItem = startDate.findItem(e => new Date(e.date).format('yyyy-MM-dd') === new Date(this.clickDate).format('yyyy-MM-dd'));
                        if (startItem) {
                            if (!startItem.holidayReason) {
                                startItem.selected = true;
                            }
                        }
                    }

                    let startItem = startDate.findItem(e => e.selected);
                    if (startItem) {
                        this.result = startItem.holdingPeriod;
                        let endItem = endDate.findItem(e => new Date(e.date).format('yyyy-MM-dd') === new Date(data.maturityDate).format('yyyy-MM-dd'));
                        endItem.selected = true;
                    }

                    this.startDate = this.calculateDate(startDate);
                    this.endDate = this.calculateDate(endDate);
                }
            }
        })
    }

    calculateDate (date) {
        let array = [];
        let firstDayOfWeek = date[0].dayOfWeek;
        let lastDayOfWeek = date[date.length - 1].dayOfWeek;
        for (let i = 0; i < firstDayOfWeek - 1; i++) {
            date.unshift(VIRTUAL_DAY);
        }
        for (let j = 0; j < 7 - lastDayOfWeek; j++) {
            date.push(VIRTUAL_DAY);
        }
        let l = date.length / 7;
        for (let k = 0; k < l; k++) {
            let sub = date.splice(0, 7);
            array.push(sub);
        }
        return array;
    }

    onDateChange (param) {
        let now = new Date();
        let months = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12];
        if (!param) {
            if (this.selectYear === this.years[0]) {
                this.selectMonth = +now.format('M');
                this.months = months.findWhere(e => e >= this.selectMonth);
            } else {
                this.months = months.findWhere(e => e <= +now.format('M'));
                this.selectMonth = this.months[0];
            }
        }

        this.onVmChanged(false);
    }
}
let babCalendar = () => {
    return {
        template: require('./template/bab_calendar.html'),
        bindings: {
            theme: '@'
        },
        controller: ['$scope', 'configConsts', 'httpService', babCalendarCtrl]
    }
};

export default babCalendar;