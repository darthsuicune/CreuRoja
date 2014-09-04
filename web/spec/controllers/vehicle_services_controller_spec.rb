require 'rails_helper'

describe VehicleServicesController do
	let(:vehicle) { FactoryGirl.create(:vehicle) }
	let(:service) { FactoryGirl.create(:service) }
	let(:user) { FactoryGirl.create(:user, role: "admin") }
	
	let(:valid_attributes) { { "vehicle_id" => vehicle.id, "service_id" => service.id } }
	let(:valid_session) { {} }

	before { sign_in user }
	
	describe "POST create" do
		it "creates a new VehicleService" do
			expect {
				post :create, { :vehicle_service => valid_attributes }, valid_session
			}.to change(VehicleService, :count).by(1)
		end
	end

	describe "DELETE destroy" do
		before(:each) do
			@vs = VehicleService.create!(:vehicle_id => vehicle.id, :service_id => service.id)
		end
		it "destroys the requested assignment" do
			expect {
				delete :destroy, { :id => @vs.id }, valid_session
			}.to change(VehicleService, :count).by(-1)
		end

		it "redirects to the service" do
			delete :destroy, { :id => @vs.id }, valid_session
			expect(response).to redirect_to(service)
		end
	end
end