class CreateVehicleServices < ActiveRecord::Migration
	def change
		create_table :vehicle_services do |t|
			t.belongs_to :vehicle
			t.belongs_to :service
			t.integer :vehicle_id
			t.integer :service_id
			t.integer :doc
			t.integer :due
			t.integer :tes
			t.integer :ci
			t.integer :asi
			t.integer :btp
			t.integer :b1
			t.integer :acu
			t.integer :per

			t.timestamps
		end
		
		add_index :vehicle_services, [:vehicle_id, :service_id], unique: true
	end
end
