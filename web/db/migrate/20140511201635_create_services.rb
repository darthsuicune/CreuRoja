class CreateServices < ActiveRecord::Migration
	def change
		create_table :services do |t|
			t.string :name
			t.string :description
			t.integer :assembly_id
			t.datetime :base_time
			t.datetime :start_time
			t.datetime :end_time
			t.string :code

			t.timestamps
		end
		add_index :services, [:assembly_id]
	end
end
