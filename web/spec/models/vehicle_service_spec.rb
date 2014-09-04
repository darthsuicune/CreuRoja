require 'rails_helper'

describe VehicleService do
	let(:vehicle_service) { FactoryGirl.create(:vehicle_service) }
	it { should respond_to(:vehicle) }
	it { should respond_to(:service) }
	it { should respond_to(:doc) }
	it { should respond_to(:due) }
	it { should respond_to(:tes) }
	it { should respond_to(:ci) }
	it { should respond_to(:asi) }
	it { should respond_to(:btp) }
	it { should respond_to(:b1) }
	it { should respond_to(:acu) }
	it { should respond_to(:per) }
end
