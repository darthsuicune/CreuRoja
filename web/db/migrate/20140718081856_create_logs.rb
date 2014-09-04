class CreateLogs < ActiveRecord::Migration
	def change
		create_table :logs do |t|
			t.integer :user_id
			t.string :action
			t.string :ip

			t.timestamps
		end
		add_index :logs, [:user_id]
		add_index :logs, [:action]
		add_index :logs, [:ip]
	end
end
