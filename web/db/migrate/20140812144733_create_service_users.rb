class CreateServiceUsers < ActiveRecord::Migration
	def change
		create_table :service_users do |t|
			t.integer :user_id
			t.integer :service_id
			t.integer :location_id

			t.timestamps
		end
		
		add_index :service_users, [:user_id, :service_id], unique: true
		add_index :service_users, :user_id
		add_index :service_users, :service_id
		add_index :service_users, :location_id
	end
end
