class AddArchivedToServices < ActiveRecord::Migration
	def change
		add_column :services, :archived, :boolean, :default => false
	end
end
