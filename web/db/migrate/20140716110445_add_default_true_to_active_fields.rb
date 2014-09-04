class AddDefaultTrueToActiveFields < ActiveRecord::Migration
  def change
	  change_column :users, :active, :boolean, :default => true
	  change_column :locations, :active, :boolean, :default => true
	  change_column :vehicles, :operative, :boolean, :default => true
  end
end
