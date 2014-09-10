require 'rails_helper'

RSpec.describe VehicleAssembliesController, :type => :controller do
	let(:admin) { FactoryGirl.create(:admin) }
	let(:vehicle) { FactoryGirl.create(:vehicle) }
	let(:assembly) { FactoryGirl.create(:assembly) }
	
	let(:attributes) { { vehicle_id: vehicle.id, location_id: assembly.id } }
	
	before {
		sign_in admin
	}
	describe "POST create" do
		it "returns http success" do
			post :create, { vehicle_assembly: attributes }
			expect(response).to redirect_to(vehicle)
		end
	end

	describe "PUT update" do
		let(:vehicle_assembly) { FactoryGirl.create(:vehicle_assembly, vehicle_id: 157) }
		it "returns http success" do
			put :update, { id: vehicle_assembly, vehicle_assembly: attributes }
			expect(VehicleAssembly.last.vehicle_id).to eq(vehicle.id)
		end
	end

	describe "DELETE destroy" do
		let(:vehicle_assembly) { FactoryGirl.create(:vehicle_assembly) }
		before { 
			vehicle_assembly.vehicle_id = vehicle.id
			vehicle_assembly.save 
		}
		it "returns http success" do
			expect {
				delete :destroy, { id: vehicle_assembly.id }
			}.to change(VehicleAssembly, :count).by(-1)
		end
	end

end
