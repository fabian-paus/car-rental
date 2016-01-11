[condition][Customer]there is a customer with that request=$c : Customer( ) from $customers
[consequence][]the customer is a driving novice=insert( new Novice($c) );
[condition][Customer]- age is less than {age} years or has driving license for less than {years} years=age < {age} || drivingLicense < {years}
[condition][RentalRequest]there is a request=$r : RentalRequest( $customers : customers, $days : days )
[condition][]there exists a customer who is driving novice=exists( Novice( customer memberOf $customers ) )
[consequence][]treat the request as novice=$r.setNovice(true);
[consequence][]set extra charge to {percent}%=$r.setExtraChargePercent({percent});
[condition][RentalRequest]- car class is not in {classes}=carClass not in {classes}
[consequence][]ask the boss for permission=$r.setRequiresNovicePermission(true);
[condition][RentalRequest]- car class is {class}=carClass == {class}
[condition][RentalRequest]- car is available=isAvailable(garage, carClass)
[condition][RentalRequest]- car is not available=!isAvailable(garage, carClass)
[consequence][]set extra deduction to {percent}%=$r.setExtraDeductionPercent( {percent});
[consequence][]set car class to {class}=$r.setCarClass({class});
[consequence][]update the request=update( $r )
[condition][]- {class} car is available=isAvailable(garage, {class})
[condition][]there exists no upgrade=not (exists Upgrade( ))
[consequence][]upgrade to {class} is possible=insert( new Upgrade({class}) );
[condition][]there is a possible upgrade=Upgrade( $upgradeClass : carClass )
[consequence][]upgrade the car class=$r.setUpgradeClass($upgradeClass);
[condition][]there is a rental day from that request=$d : RentalDay( ) from $days
[consequence][]set the daily price to {price}=$d.setPrice((int)Math.round(100 * {price}));
[condition][RentalDay]- is weekend or holiday=isWeekend(day) || isHoliday(day)
[consequence][]the daily price is discounted by {percent}%=$d.setPrice(Math.round((100 - {percent}) / 100.0f * $d.getPrice()));
[condition][]- day index is in {indices}=dayIndex in {indices}
[consequence][]add the daily price to the base price=$r.addToBasePrice($d.getPrice());
[condition][]every customer from that request is a new customer=forall( Customer( newCustomer ) from $customers )
[consequence][]add {discount} to the discount=$r.addToDiscount((int)Math.round( {discount} * 100));
[condition][]every customer from that request had a valid reclamation=forall ( Customer( hasReclamation ) from $customers )
[consequence][]discount the base price by {percent}%=$r.addToDiscount( Math.round( {percent} / 100.0f * $r.getBasePrice() ) );
[condition][]every customer from that request had a security training=forall( Customer( hasSecurityTraining ) from $customers )
[consequence][]set the discount to {discount}=$r.setDiscount((int)Math.round(100 * {discount}));
[condition][RentalRequest]- discount is greater than {limit}=discount > {limit} * 100
[consequence][]set the final price to the base price=$r.setFinalPrice($r.getBasePrice());
[consequence][]subtract the discount from the final price=$r.addToFinalPrice(-$r.getDiscount());
[condition][RentalRequest]- for an automatic car=automatic
[consequence][]add a {percent}% fee of the base price to the final price=$r.addToFinalPrice(Math.round({percent} / 100.0f * $r.getBasePrice()));
[consequence][]mark the car for that request as available=$r.setCarAvailable(true);
[*][]set {var} to 100% plus extra charge minus extra deduction=int {var} = 100 + $r.getExtraChargePercent() - $r.getExtraDeductionPercent();
[*][]set the total price to {percent}% of the final price=$r.setTotalPrice(Math.round({percent} / 100.0f * $r.getFinalPrice()));
