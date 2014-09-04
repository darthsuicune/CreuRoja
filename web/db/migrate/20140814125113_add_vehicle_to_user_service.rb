class AddVehicleToUserService < ActiveRecord::Migration
	def change
		add_column :service_users, :vehicle_id, :integer
	end
end
