require 'user_management/user_id'
require 'user_management/user_repository'
require 'user_management/delete_user'

RSpec.describe UserManagement::DeleteUser do
  subject(:delete_user) { described_class.new(user_repository) }

  let(:user_repository) { instance_double(UserManagement::UserRepository) }
  let(:user_id)         { UserManagement::UserId.new(value: 'u1') }

  before { allow(user_repository).to receive(:delete) }

  it 'calls repository delete with the user id' do
    delete_user.call(user_id)

    expect(user_repository).to have_received(:delete).with(user_id)
  end
end
