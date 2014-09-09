require 'rails_helper'

RSpec.describe VehiclePositionsController, :type => :controller do
	let(:admin) { FactoryGirl.create(:admin) }
	before {
		sign_in admin
	}
	
	describe "GET index" do
		it "returns http success" do
			get :index
			expect(response).to be_success
		end
	end

	describe "POST create" do
		let(:vehicle) { FactoryGirl.create(:vehicle) }
		let(:values) { { "vehicle_id" => vehicle.id, "latitude" => 1, "longitude" => 1 } }
		
		it "returns http success" do
			post :create, { :vehicle_position => values, format: :json }
			expect(response).to be_success
		end
	end

end
