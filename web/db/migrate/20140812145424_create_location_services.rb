class CreateLocationServices < ActiveRecord::Migration
	def change
		create_table :location_services do |t|
			t.integer :location_id
			t.integer :service_id

			t.timestamps
		end
		add_index :location_services, [:location_id, :service_id], unique: true
	end
end
