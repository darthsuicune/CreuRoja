require 'rails_helper'

describe Location do
	before { @location = FactoryGirl.create(:location) }
	
	subject { @location }
	
	it { should respond_to("latitude") }
	it { should respond_to("longitude") }
	it { should respond_to("name") }
	it { should respond_to("address") }
	it { should respond_to("location_type") }
	it { should respond_to("services") }
	it { should respond_to("users") }
	it { should respond_to("active") }
	
	describe "with invalid parameters" do
		let(:location) { Location.new(name: "asdf", address: "asdf", latitude: 0, longitude: 0, location_type: "asdf") }
		subject { location }
		describe "without name" do
			before { location.name = nil }
			it { should_not be_valid }
		end
		describe "without address" do
			before { location.address = nil }
			it { should_not be_valid }
		end
		describe "without latitude" do
			before { location.latitude = nil }
			it { should_not be_valid }
		end
		describe "without longitude" do
			before { location.longitude = nil }
			it { should_not be_valid }
		end
		describe "without type" do
			before { location.location_type = nil }
			it { should_not be_valid }
		end
	end
end
