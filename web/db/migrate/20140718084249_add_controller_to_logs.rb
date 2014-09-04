class AddControllerToLogs < ActiveRecord::Migration
	def change
		add_column :logs, :controller, :string
		add_index :logs, [:controller]
	end
end
