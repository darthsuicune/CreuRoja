require 'rails_helper'

RSpec.describe VehicleAssembly, :type => :model do
	let(:vehicle_assembly) { FactoryGirl.create(:vehicle_assembly) }
	subject { vehicle_assembly }
	
	it "has its required properties" do
		expect(vehicle_assembly).to respond_to (:vehicle)
		expect(vehicle_assembly).to respond_to (:location)
	end
end
