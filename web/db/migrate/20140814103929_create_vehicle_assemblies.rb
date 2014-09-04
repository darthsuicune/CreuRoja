class CreateVehicleAssemblies < ActiveRecord::Migration
	def change
		create_table :vehicle_assemblies do |t|
			t.integer :vehicle_id
			t.integer :location_id

			t.timestamps
		end
	end
end
