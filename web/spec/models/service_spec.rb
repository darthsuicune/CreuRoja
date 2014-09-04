require 'rails_helper'

describe Service do
	before { @service = FactoryGirl.create(:service) }
	subject { @service }
	
	it { should respond_to(:name) }
	it { should respond_to(:assembly_id) }
	it { should respond_to(:base_time) }
	it { should respond_to(:start_time) }
	it { should respond_to(:end_time) }
	it { should respond_to(:vehicles) }
	
	describe "with invalid parameters" do
		let(:service) { Service.new(name: "asdf", assembly_id: 0, base_time: Time.now, start_time: Time.now, end_time: Time.now) }
		subject { service }
		
		describe "without name" do
			before { service.name = nil }
			it { should_not be_valid }
		end
		describe "without assembly_id" do
			before { service.assembly_id = nil }
			it { should_not be_valid }
		end
		describe "without base_time" do
			before { service.base_time = nil }
			it { should_not be_valid }
		end
		describe "without start_time" do
			before { service.start_time = nil }
			it { should_not be_valid }
		end
		describe "without end_time" do
			before { service.end_time = nil }
			it { should_not be_valid }
		end
	end
end
